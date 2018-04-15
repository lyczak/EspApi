ESP_URL=$1
ESP_USER=$2
ESP_PASS=$3

curl "$ESP_URL/Account/LogOn" \
  -H 'Origin: https://hac.pvsd.org' \
  -H 'Accept-Encoding: gzip, deflate, br' \
  -H 'Accept-Language: en-US,en;q=0.9' \
  -H 'Upgrade-Insecure-Requests: 1' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36 OPR/51.0.2830.55' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8' \
  -H 'Cache-Control: max-age=0' \
  -H 'Referer: https://hac.pvsd.org/HomeAccess/Account/LogOn' \
  -H 'Connection: keep-alive' \
  --data "Database=10&LogOnDetails.UserName=$ESP_USER&LogOnDetails.Password=$ESP_PASS" \
  --compressed \
  --cookie-jar Cookies.txt \
  -L
