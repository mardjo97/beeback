# ---------- BUILD STAGE ----------
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /build

# install node for frontend build
RUN apk add --no-cache nodejs npm

# copy only files needed for dependency resolution (cache layer)
COPY pom.xml .
COPY sonar-project.properties .

# download maven dependencies
RUN mvn -B -q -e -DskipTests dependency:go-offline

# copy source
COPY src ./src

# copy frontend files (required when frontend is enabled)
COPY package.json package-lock.json angular.json tsconfig.json tsconfig.app.json tsconfig.spec.json ngsw-config.json jest.conf.js eslint.config.mjs ./
COPY webpack ./webpack

# disable desktop notifications in container (avoids node-notifier spawn E2BIG)
ENV CI=true

# build jar with frontend (prod profile uses webapp:prod, avoids node-notifier E2BIG in dev)
RUN mvn -B -DskipTests -Dsonar.skip=true -Dmaven.test.skip=true -Pprod package


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# security: run as non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# copy jar from build stage
COPY --from=build /build/target/*jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]