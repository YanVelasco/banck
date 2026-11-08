{{- define "bank-common.service" -}}
{{- if .Values.service.enabled }}
apiVersion: v1
kind: Service
metadata:
  name: {{ .Values.serviceName }}
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "bank-common.labels" (dict "ctx" . "name" .Values.appLabel) | nindent 4 }}
spec:
  type: {{ default "ClusterIP" .Values.service.type }}
  selector:
    app: {{ .Values.appLabel }}
  ports:
    - name: http
      port: {{ .Values.service.port }}
      targetPort: {{ .Values.service.targetPort }}
      protocol: TCP
      {{- with .Values.service.nodePort }}
      nodePort: {{ . }}
      {{- end }}
{{- end }}
{{- end }}
