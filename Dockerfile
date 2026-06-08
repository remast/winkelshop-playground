FROM node:22-alpine

# Install opencode globally via npm
RUN npm install -g opencode-ai

WORKDIR /project

# Disable auto-update inside the container — the version is managed via the image
ENV OPENCODE_CONFIG_CONTENT='{"autoupdate":false}'

ENTRYPOINT ["opencode"]
