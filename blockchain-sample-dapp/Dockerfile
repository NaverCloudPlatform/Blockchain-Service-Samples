FROM node:12
WORKDIR /usr/src/app

COPY . .
WORKDIR /usr/src/app/frontend
RUN npm install
RUN npm run build
WORKDIR /usr/src/app/backend
RUN npm install
EXPOSE 3000
CMD ["./start.sh"]