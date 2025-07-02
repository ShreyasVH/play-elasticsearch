rm -rf dist

while true; do
	sbt clean compile dist

	if [[ -e target/universal/$REPO_NAME-$REPO_VERSION.zip ]]; then
		unzip target/universal/$REPO_NAME-$REPO_VERSION.zip > /dev/null
		mv $REPO_NAME-$REPO_VERSION dist
		break
	fi
done
