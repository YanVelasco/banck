$ErrorActionPreference = 'Stop'

$logDirectory = Join-Path $env:LOCALAPPDATA 'Bank'
$logPath = Join-Path $logDirectory 'gateway-port-forward.log'
New-Item -ItemType Directory -Force -Path $logDirectory | Out-Null

$kubectl = (Get-Command kubectl.exe -ErrorAction Stop).Source
$lastHealthy = $false

while ($true) {
    try {
        $response = Invoke-WebRequest `
            -Uri 'http://127.0.0.1:8072/actuator/health' `
            -UseBasicParsing `
            -TimeoutSec 3

        if ($response.StatusCode -eq 200) {
            if (-not $lastHealthy) {
                Add-Content -Path $logPath -Value "$(Get-Date -Format o) Gateway healthy at http://localhost:8072"
                $lastHealthy = $true
            }

            Start-Sleep -Seconds 10
            continue
        }
    }
    catch {
        if ($lastHealthy) {
            Add-Content -Path $logPath -Value "$(Get-Date -Format o) Gateway health check failed: $($_.Exception.Message)"
            $lastHealthy = $false
        }
    }

    Add-Content -Path $logPath -Value "$(Get-Date -Format o) Starting kubectl port-forward on localhost:8072"
    & $kubectl port-forward --address 127.0.0.1 svc/gatewayserver 8072:8072 -n bank *>> $logPath
    Add-Content -Path $logPath -Value "$(Get-Date -Format o) kubectl port-forward exited with code $LASTEXITCODE; retrying in 5 seconds"
    Start-Sleep -Seconds 5
}
