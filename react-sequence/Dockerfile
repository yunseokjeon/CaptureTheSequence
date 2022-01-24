# pull the official base image  
FROM node:16.13.0-alpine
 
# set your working directory  
WORKDIR /app  
 
# add `/app/node_modules/.bin` to $PATH  
ENV PATH /app/node_modules/.bin:$PATH  
 
# install application dependencies  
COPY package.json ./  
COPY package-lock.json ./  
RUN npm install
RUN npm install react-scripts@3.0.1 -g
 
# add app  
COPY . ./  
 
# will start app  
CMD ["npm", "start"] 