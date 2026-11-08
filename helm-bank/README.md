# Helm charts do banco

Cada diretório em `bank-services` contém um chart independente para o
microsserviço correspondente. O chart agregador `bank-platform` instala os
sete serviços em conjunto. Imagens, portas, probes e recursos ficam no
`values.yaml` do serviço correspondente. A biblioteca `bank-common` centraliza
os templates reutilizáveis de `Deployment`, `Service` e labels, além de todas
as variáveis de ambiente específicas e compartilhadas em
`bank-common/values.yaml`, agrupadas por aplicação. Os charts de serviço
incluem essa dependência local e mantêm apenas pequenos arquivos que chamam os
templates compartilhados.

Os charts individuais seguem a estrutura de configuração do
`helm-eazybank/eazybank-services`: `deploymentName`, `serviceName`, `appLabel`,
`replicaCount`, imagem com `repository`/`tag`, configuração de Service,
recursos, probes e flags de integrações. Os valores de imagem, portas,
recursos, probes e integrações continuam sendo os específicos do Bank. O
serviço `message` é consumidor Kafka sem endpoint HTTP e, como no manifesto
Kubernetes do Bank, não cria um Service nem define uma porta de container.

O chart agregador cria o ConfigMap `bank-app-config` e o Secret `bank-secrets`,
usando os valores configurados em `bank-common/values.yaml`. As aplicações leem
configurações comuns do ConfigMap e os serviços de banco leem usuário e senha
das chaves do Secret. Os valores padrão do Secret são
credenciais de desenvolvimento: altere-os em um arquivo de valores privado
antes de instalar em qualquer ambiente compartilhado ou de produção. Helm
Secrets não são um cofre para credenciais. Para substituir valores do pacote
comum compartilhados (ConfigMap e Secret) na instalação agregada, use a chave
`bank-common` no arquivo de valores, por exemplo
`bank-common.secrets.data.mysql-password`. Para substituir uma variável
específica de uma aplicação, use o nome do subchart seguido por
`bank-common.applications.<serviço>.env`; por exemplo:

```powershell
helm upgrade --install bank .\helm-bank\bank-platform --namespace bank --create-namespace -f .\helm-bank\bank-platform\values\values-prod.yml --set-string "accounts.bank-common.applications.accounts.env[2].value=jdbc:mysql://mysql-accounts-qa:3306/accountsdb"
```

Os charts implantam as aplicações, mas esperam que os serviços de infraestrutura
(bancos, Kafka, Redis, Keycloak e Tempo) já existam no namespace escolhido.

Na raiz do repositório, baixe/empacote as dependências locais do chart:

```powershell
helm dependency update .\helm-bank\bank-platform
helm lint .\helm-bank\bank-platform
helm template bank .\helm-bank\bank-platform --namespace bank -f .\helm-bank\bank-platform\values\values-prod.yml
```

Para instalar ou atualizar em produção:

```powershell
helm upgrade --install bank .\helm-bank\bank-platform --namespace bank --create-namespace -f .\helm-bank\bank-platform\values\values-prod.yml
```

Para QA, substitua `values-prod.yml` por `values-qa.yml`. Um chart individual
pode ser instalado diretamente, mas o ConfigMap `bank-app-config` e o Secret
`bank-secrets` precisam já existir no namespace:

```powershell
helm upgrade --install accounts .\helm-bank\bank-services\accounts --namespace bank --set global.partOf=bank --set global.springProfile=prod
```

No Windows, você pode manter o gateway acessível em `http://localhost:8072`
sem iniciar o `kubectl port-forward` manualmente em cada sessão. Registre uma
tarefa agendada para iniciá-lo no login do usuário:

```powershell
$script = (Resolve-Path .\helm-bank\scripts\start-gateway-port-forward.ps1).Path
$user = "$env:USERDOMAIN\$env:USERNAME"
$action = New-ScheduledTaskAction -Execute "powershell.exe" -Argument "-NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -File `"$script`""
$trigger = New-ScheduledTaskTrigger -AtLogOn -User $user
$settings = New-ScheduledTaskSettingsSet -ExecutionTimeLimit ([TimeSpan]::Zero) -RestartCount 999 -RestartInterval (New-TimeSpan -Minutes 1)
Register-ScheduledTask -TaskName "Bank Gateway Port Forward" -Action $action -Trigger $trigger -Settings $settings -Description "Keeps the Bank gateway available at localhost:8072" -Force
Start-ScheduledTask -TaskName "Bank Gateway Port Forward"
```

O script verifica `/actuator/health` e reabre o encaminhamento se necessário.
O log fica em `$env:LOCALAPPDATA\Bank\gateway-port-forward.log`. A tarefa
funciona enquanto o usuário estiver conectado ao Windows e o cluster Docker
Desktop estiver ativo. Os microsserviços de banco leem usuário e senha das
chaves `mysql-username` e `mysql-password` do Secret `bank-secrets`. Não instale
o chart agregador junto com outra instalação que
já gerencie os mesmos Deployments e Services.