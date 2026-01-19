curl -X PUT "https://$ELASTIC_USERNAME:$ELASTIC_PASSWORD@$ELASTIC_IP_HTTP:$ELASTIC_PORT_HTTP/$ELASTIC_INDEX_BOOKS" -H "Content-Type: application/json" -d @conf/books.json > /dev/null 2>&1
RESPONSE=$(curl -u "$ELASTIC_USERNAME:$ELASTIC_PASSWORD" -X POST "https://$ELASTIC_IP_HTTP:$ELASTIC_PORT_HTTP/_security/api_key" -H "Content-Type: application/json" -d '{ "name": "cluster-wide-writer", "expiration": "90d", "role_descriptors": { "cluster_writer_role": { "cluster": [ "monitor" ], "index": [ { "names": ["'$ELASTIC_INDEX_BOOKS'"], "privileges": [ "all" ] } ] } } }')
export PATH=$HOME/programs/jq/$JQ_VERSION/bin:$PATH
API_KEY=$(echo "$RESPONSE" | jq -r '.encoded')
echo "export ELASTIC_API_KEY=\"$API_KEY\"" >> .envrc