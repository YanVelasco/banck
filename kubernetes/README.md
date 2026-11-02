# Kubernetes deployment

These manifests deploy the bank services and the supporting services from the
default Compose stack into the `bank` namespace.

Apply everything with:

```powershell
kubectl apply -k .\kubernetes
kubectl -n bank get pods
```

The database, Kafka, Redis, Keycloak, MinIO, Loki, Tempo, Prometheus, and
Grafana manifests use PersistentVolumeClaims and require a cluster with a
default StorageClass. Redis uses AOF persistence. Keycloak's development-mode
database is stored under `/opt/keycloak/data` on its PVC.
All containers specify Kubernetes CPU and memory requests/limits based on the
Compose resource settings; Keycloak has additional headroom for startup.
Services are `ClusterIP`; access them locally with port-forwarding, for example:

```powershell
kubectl -n bank port-forward svc/gatewayserver 8072:8072
kubectl -n bank port-forward svc/grafana 3000:3000
kubectl -n bank port-forward svc/kafka 9092:9092
```

On Windows, `gateway-port-forward.ps1` can keep the Gateway tunnel running and
retry it if the connection drops. Register it in Task Scheduler to start at
sign-in, or run it manually from PowerShell. It writes logs to
`%LOCALAPPDATA%\Banck\gateway-port-forward.log`.

`keycloak-port-forward.ps1` provides the same automatic tunnel and retry for
Keycloak at `http://localhost:8080`; its log is
`%LOCALAPPDATA%\Banck\keycloak-port-forward.log`.

`kafka-port-forward.ps1` keeps the Kafka external listener available at
`localhost:9092`; use that as the bootstrap server for local clients. Its log
is `%LOCALAPPDATA%\Banck\kafka-port-forward.log`.

The Secret uses the development credentials from the Compose setup. Kubernetes
Secrets are not encrypted in Git or by base64 encoding; replace these defaults
and configure encryption at rest or an external secret manager before using
these manifests in a production cluster.
