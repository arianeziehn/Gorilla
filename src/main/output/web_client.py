
import requests


url = "http://gorilla.bigdama.tu-berlin.de:8001/DuplicateDetectionEvaluator/"
output = open("csvOutput5.csv", "r").read()
r = requests.post(url, data={"value": output})
if r.status_code != 200:
	print "There is an error! The error code:", r.status_code
print r.text
