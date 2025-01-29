FROM nginx:stable-alpine

LABEL maintainer = "Fabio Peixoto <fabio.silva@empresta.com.br>"


COPY nginx.conf /etc/nginx/

RUN echo "upstream php-upstream { server php:9000; }" > /etc/nginx/conf.d/upstream.conf

# ensure www-data user exists
RUN set -x ; \
  addgroup -g 82 -S www-data ; \
  adduser -u 82 -D -S -G www-data www-data && exit 0 ; exit 1


CMD ["nginx"]

EXPOSE 80
EXPOSE 443
