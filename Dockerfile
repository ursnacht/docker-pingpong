FROM airhacks/payara5
COPY ./target/pingpong.war ${DEPLOYMENT_DIR}
