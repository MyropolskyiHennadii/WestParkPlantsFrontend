#From VS Help
#FROM node:12.18-alpine
#ENV NODE_ENV=production
#WORKDIR /usr/src/app
##COPY ["package.json", "package-lock.json*", "npm-shrinkwrap.json*", "./"]
#RUN npm install --production --silent && mv node_modules ../
#COPY . .
#EXPOSE 3000
#CMD ["npm", "start"]

#https://dzone.com/articles/how-to-dockerize-reactjs-app
# Step 1
FROM node:10-alpine as build-step
RUN mkdir /app

WORKDIR /app
COPY package.json /app

RUN npm install

COPY . /app
RUN npm run build
 
# Stage 2
FROM nginx:1.17.1-alpine
COPY --from=build-step /app/build /usr/share/nginx/html