$ErrorActionPreference = 'Stop'

$logDirectory = Join-Path $env:LOCALAPPDATA 'Banck'
$logPath = Join-Path $logDirectory 'kafka-port-forward.log'

New-Item -ItemType Directory -Path $logDirectory -Force | Out-Null

while ($true) {
    try {
        Add-Content -Path $logPath -Encoding UTF8 -Value "$(Get-Date -Format o) Starting Kafka port-forward."
        & kubectl -n bank port-forward svc/kafka 9092:9092 2>&1 |
            ForEach-Object { Add-Content -Path $logPath -Encoding UTF8 -Value $_ }
        Add-Content -Path $logPath -Encoding UTF8 -Value "$(Get-Date -Format o) Port-forward exited with code $LASTEXITCODE."
    }
    catch {
        Add-Content -Path $logPath -Encoding UTF8 -Value "$(Get-Date -Format o) Port-forward failed: $($_.Exception.Message)"
    }

    Start-Sleep -Seconds 5
}
