{{- define "bank-common.service" -}}
{{- if .Values.service.enabled }}
apiVersion: v1
kind: Service
metadata:
  name: {{ .Chart.Name }}
  labels:
    {{- include "bank-common.labels" . | nindent 4 }}
spec:
  type: {{ default "ClusterIP" .Values.service.type }}
  selector:
    app: {{ .Chart.Name }}
  ports:
    - name: http
      port: {{ .Values.service.port }}
      targetPort: http
      protocol: TCP
      {{- with .Values.service.nodePort }}
      nodePort: {{ . }}
      {{- end }}
{{- end }}
{{- end }}
