IMAGE := qa-wp:1.0.0
REPO   := redbook-docker-dev.artifacts.com

create:
	docker build . -t  $(REPO)/$(IMAGE)

publish:
	docker push $(REPO)/$(IMAGE)

.PHONY: \
	create
	publish
