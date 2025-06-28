# Stage 1: Build Next.js app
FROM node:lts-alpine3.21 AS builder

WORKDIR /app

# Copy package files from context (relative to build context root)
COPY zeover-ui/package.json zeover-ui/package-lock.json* zeover-ui/yarn.lock* ./

RUN npm install

# Copy the rest of the app source code from zeover-ui folder
COPY zeover-ui .

RUN npm run build

# Stage 2: Run Next.js app
FROM node:lts-alpine3.21

RUN apk update && apk upgrade --no-cache

WORKDIR /app

COPY zeover-ui/package.json zeover-ui/package-lock.json* zeover-ui/yarn.lock* ./
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/.next ./.next
# COPY --from=builder /app/public ./public

EXPOSE 3000

CMD ["npm", "start"]
