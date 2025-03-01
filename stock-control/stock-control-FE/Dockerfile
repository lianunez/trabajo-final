FROM node:alpine
WORKDIR /usr/src/app
COPY . .
RUN npm install -g live-server
EXPOSE 5500
CMD ["live-server", "--host=0.0.0.0", "--port=5500"]