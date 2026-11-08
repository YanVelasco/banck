{{- define "bank-common.deployment" -}}
{{- $common := (index .Subcharts "bank-common").Values -}}
{{- $application := index $common.applications .Chart.Name -}}
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ .Values.deploymentName }}
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "bank-common.labels" (dict "ctx" . "name" .Values.appLabel) | nindent 4 }}
spec:
  replicas: {{ .Values.replicaCount }}
  selector:
    matchLabels:
      app: {{ .Values.appLabel }}
  template:
    metadata:
      labels:
        {{- include "bank-common.podLabels" (dict "ctx" . "name" .Values.appLabel) | nindent 8 }}
    spec:
      containers:
        - name: {{ .Values.appLabel }}
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
          imagePullPolicy: {{ .Values.imagePullPolicy }}
          {{- if .Values.containerPort }}
          ports:
            - containerPort: {{ .Values.containerPort }}
          {{- end }}
          env:
            {{- if .Values.profileEnabled }}
            - name: SPRING_PROFILES_ACTIVE
              value: {{ $common.global.springProfile | quote }}
            {{- end }}
            {{- if .Values.databaseEnabled }}
            - name: SPRING_DATASOURCE_DRIVER_CLASS_NAME
              valueFrom:
                configMapKeyRef:
                  name: bank-app-config
                  key: SPRING_DATASOURCE_DRIVER_CLASS_NAME
            - name: SPRING_JPA_DATABASE_PLATFORM
              valueFrom:
                configMapKeyRef:
                  name: bank-app-config
                  key: SPRING_JPA_DATABASE_PLATFORM
            {{- end }}
            {{- if .Values.eurekaEnabled }}
            - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
              valueFrom:
                configMapKeyRef:
                  name: bank-app-config
                  key: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
            {{- end }}
            {{- if .Values.kafkaEnabled }}
            - name: SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS
              valueFrom:
                configMapKeyRef:
                  name: bank-app-config
                  key: SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS
            {{- end }}
            {{- if .Values.otelEnabled }}
            - name: OTEL_EXPORTER_OTLP_ENDPOINT
              valueFrom:
                configMapKeyRef:
                  name: bank-app-config
                  key: OTEL_EXPORTER_OTLP_ENDPOINT
            - name: OTEL_EXPORTER_OTLP_PROTOCOL
              valueFrom:
                configMapKeyRef:
                  name: bank-app-config
                  key: OTEL_EXPORTER_OTLP_PROTOCOL
            - name: OTEL_LOGS_EXPORTER
              valueFrom:
                configMapKeyRef:
                  name: bank-app-config
                  key: OTEL_LOGS_EXPORTER
            - name: OTEL_METRICS_EXPORTER
              valueFrom:
                configMapKeyRef:
                  name: bank-app-config
                  key: OTEL_METRICS_EXPORTER
            - name: OTEL_TRACES_EXPORTER
              valueFrom:
                configMapKeyRef:
                  name: bank-app-config
                  key: OTEL_TRACES_EXPORTER
            {{- end }}
            {{- with $application.env }}
            {{- range $env := . }}
            {{- if or (ne $env.name "SPRING_CONFIG_IMPORT") $.Values.configImportEnabled }}
            {{- if or (ne $env.name "SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI") $.Values.jwkEnabled }}
            {{- if or (not (hasPrefix "SPRING_DATA_REDIS_" $env.name)) $.Values.redisEnabled }}
            {{- toYaml (list $env) | nindent 12 }}
            {{- end }}
            {{- end }}
            {{- end }}
            {{- end }}
            {{- end }}
          {{- with .Values.startupProbe }}
          startupProbe:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          {{- with .Values.readinessProbe }}
          readinessProbe:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          {{- with .Values.livenessProbe }}
          livenessProbe:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          {{- with .Values.resources }}
          resources:
            {{- toYaml . | nindent 12 }}
          {{- end }}
{{- end }}
