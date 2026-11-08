{{- define "bank-common.deployment" -}}
{{- $common := (index .Subcharts "bank-common").Values -}}
{{- $application := index $common.applications .Chart.Name -}}
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ .Chart.Name }}
  labels:
    {{- include "bank-common.labels" . | nindent 4 }}
spec:
  replicas: {{ .Values.replicas }}
  selector:
    matchLabels:
      app: {{ .Chart.Name }}
  template:
    metadata:
      labels:
        app: {{ .Chart.Name }}
        app.kubernetes.io/name: {{ .Chart.Name }}
        app.kubernetes.io/instance: {{ .Release.Name }}
    spec:
      containers:
        - name: {{ .Chart.Name }}
          image: {{ .Values.image | quote }}
          imagePullPolicy: {{ .Values.imagePullPolicy }}
          ports:
            - name: http
              containerPort: {{ .Values.containerPort }}
              protocol: TCP
          {{- if ne .Chart.Name "config-server" }}
          envFrom:
            - configMapRef:
                name: bank-app-config
          {{- end }}
          env:
            {{- if ne .Chart.Name "config-server" }}
            - name: SPRING_PROFILES_ACTIVE
              value: {{ $common.global.springProfile | quote }}
            {{- end }}
            {{- with $application.env }}
            {{- toYaml . | nindent 12 }}
            {{- end }}
          {{- with .Values.startupProbe }}
          startupProbe:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          {{- with .Values.readinessProbe }}
          readinessProbe:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          {{- with .Values.resources }}
          resources:
            {{- toYaml . | nindent 12 }}
          {{- end }}
{{- end }}
