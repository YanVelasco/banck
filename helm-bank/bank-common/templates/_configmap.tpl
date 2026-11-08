{{- define "bank-common.configmap" -}}
{{- $common := (index .Subcharts "bank-common").Values -}}
apiVersion: v1
kind: ConfigMap
metadata:
  name: bank-app-config
  labels:
    {{- include "bank-common.commonLabels" . | nindent 4 }}
data:
  {{- toYaml $common.appConfig | nindent 2 }}
{{- end }}
