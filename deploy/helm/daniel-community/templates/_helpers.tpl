{{/*
Return the chart name.
*/}}
{{- define "daniel-community.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Return a fully qualified application name.
*/}}
{{- define "daniel-community.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Return the chart name and version.
*/}}
{{- define "daniel-community.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels.
*/}}
{{- define "daniel-community.labels" -}}
helm.sh/chart: {{ include "daniel-community.chart" . }}
{{ include "daniel-community.selectorLabels" . }}
{{- with .Chart.AppVersion }}
app.kubernetes.io/version: {{ . | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Immutable selector labels.
*/}}
{{- define "daniel-community.selectorLabels" -}}
app.kubernetes.io/name: {{ include "daniel-community.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
ConfigMap name.
*/}}
{{- define "daniel-community.configMapName" -}}
{{- printf "%s-config" (include "daniel-community.fullname" .) | trunc 63 | trimSuffix "-" }}
{{- end }}
