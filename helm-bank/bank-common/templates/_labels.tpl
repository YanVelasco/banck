{{- define "bank-common.partOf" -}}
{{- $partOf := "bank" -}}
{{- with .Values.global -}}
{{- $partOf = .partOf | default "bank" -}}
{{- end -}}
{{- $partOf -}}
{{- end }}

{{- define "bank-common.commonLabels" -}}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/part-of: {{ include "bank-common.partOf" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{- define "bank-common.labels" -}}
app: {{ .name }}
app.kubernetes.io/name: {{ .name }}
{{ include "bank-common.commonLabels" .ctx }}
{{- end }}

{{- define "bank-common.podLabels" -}}
app: {{ .name }}
app.kubernetes.io/name: {{ .name }}
app.kubernetes.io/instance: {{ .ctx.Release.Name }}
app.kubernetes.io/part-of: {{ include "bank-common.partOf" .ctx }}
{{- end }}
