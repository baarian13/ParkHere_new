import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import json

class SearchSpotDateTimeHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        results = [{'id'       : res[0],
                    'address'  : res[1],
                    'start'    : str(res[2]),
                    'end'      : str(res[3])}
            for res in self.db.searchForSpotsDateTime(self.get_argument("start"), self.get_argument("end"))]
        self.write(json.dumps(results))
