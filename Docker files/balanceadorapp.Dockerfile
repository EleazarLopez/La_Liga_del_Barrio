FROM haproxytech/haproxy-alpine:2.4
COPY haproxy.cfg /usr/local/conf.d/load-balancer.conf