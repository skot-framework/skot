echo "1 - Préparation du repository local"
cd $1
echo "checkout"
git checkout repository
echo "pull"
git pull
echo "nettoyage repo maven local"
cd $2
rm -r tech
rm -r skot-*
cp -r $1/tech .
cp -r $1/skot-* .
echo "2 - publish in local repository"
cd $4
./gradlew publishToMavenLocal
echo "3 - Push to GitHub"
cd $1
echo "mise à jour le la branche repository"
rm -r tech
rm -r skot-*
cp -r ~/.m2/repository/tech .
cp -r ~/.m2/repository/skot-* .
echo "commit"
git add -A
git commit -m "v"$3
echo "push"
git push origin repository
echo "taggage branche courante"
cd $4
git tag v$3
git push origin v$3
echo "done"