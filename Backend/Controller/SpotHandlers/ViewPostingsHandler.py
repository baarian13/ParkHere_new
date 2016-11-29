import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import json


class ViewPostingsHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        ownerEmail = self.get_argument('email')
        if ownerEmail:
            results = [{'id'       : res[0],
                        'address'  : res[1]}
                       for res in self.db.getSpotsOwnedBy(ownerEmail)]
            self.write(json.dumps(results))
