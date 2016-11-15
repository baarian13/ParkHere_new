import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import json


class ViewSpotHistoryHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        renterEmail = self.get_argument("email")
        if renterEmail:
            results = self.db.getSpotHistoryIDsRentedBy(renterEmail)
            self.write(json.dumps(results))
