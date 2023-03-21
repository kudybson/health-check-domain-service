@echo on
minikube status
helm install health-check-domain-service config/ --values config/values.yaml
