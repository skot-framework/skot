echo "Push to GitHub"
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
echo "done"