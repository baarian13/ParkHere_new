import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import json


class ViewSpotHistoryHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        renterEmail = self.get_argument("email")
        if renterEmail:
            results = [{'id'       : res[0],
                        'address'  : res[1]}
            for res in self.db.getSpotHistoryRentedBy(renterEmail)]
            self.write(json.dumps(results))
