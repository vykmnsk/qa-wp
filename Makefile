IMAGE := qa-wagerplayer:1.0.0
REPO   := redbook-docker-dev.artifacts.tabdigital.com.au

create:
	docker build . -t  $(REPO)/$(IMAGE)

publish:
	docker push $(REPO)/$(IMAGE)

.PHONY: \
	create
	publish
