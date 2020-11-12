fuser -k 35504/tcp || true
serve -s test-client/dist/ -l 35504
