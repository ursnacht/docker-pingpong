FROM airhacks/payara5
COPY ./target/znueni.war ${DEPLOYMENT_DIR}
