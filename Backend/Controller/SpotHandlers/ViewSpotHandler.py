import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import json


class ViewSpotHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        spotID = self.get_argument("spotID")
        #needs to be finished
        if spotID:
            results = [{'address'       : res[0],
                        'latitude'      : res[1],
                        'longitude'     : res[2],
                        'picture'       : res[3],
                        'phoneNumber'   : res[4],
                        'start'         : str(res[5]),
                        'end'           : str(res[6]),
                        'description'   : res[7],
                        'price'         : res[8],
                        'ownerEmail'    : res[9]}
            for res in self.db.viewSpotInfo(spotID)]
            res2 = self.db.viewSpotRating(results["ownerEmail"])
            results["ownerRating"] = res2[0]
            self.write(json.dumps(results))