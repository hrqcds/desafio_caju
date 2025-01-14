FROM node:lts-alpine

# Create app directory
WORKDIR /usr/app

COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 9000

RUN npm run build

CMD ["npm", "start"]