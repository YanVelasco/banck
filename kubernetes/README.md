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
Gateway, Keycloak, and Kafka are exposed locally with `NodePort` services:

- Gateway: `http://localhost:30072`
- Keycloak: `http://localhost:30080`
- Kafka bootstrap server: `localhost:30092`

The other services remain `ClusterIP` and are reachable only inside Kubernetes.
Kafka's in-cluster clients continue to use `kafka:29092`; the external listener
advertises `localhost:30092` for clients on the Docker Desktop host.

NodePorts bind on the Kubernetes node interfaces. Kafka is configured for
plaintext in this development setup, so do not expose these ports on an
untrusted network or use this configuration in production.

The Secret uses the development credentials from the Compose setup. Kubernetes
Secrets are not encrypted in Git or by base64 encoding; replace these defaults
and configure encryption at rest or an external secret manager before using
these manifests in a production cluster.
