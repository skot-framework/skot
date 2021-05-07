echo "Préparation du repository local"
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
echo "done"
