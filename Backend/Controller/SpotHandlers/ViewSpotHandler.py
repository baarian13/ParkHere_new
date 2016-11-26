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
            res = self.db.viewSpotInfo(spotID)
            results = { 'address'           : res[0],
                        'start'             : str(res[1].strftime('%Y-%m-%d %H:%M:%S')),
                        'end'               : str(res[2].strftime('%Y-%m-%d %H:%M:%S')),
                        'spotType'          : res[3],
                        'ownerEmail'        : res[4],
                        'renterEmail'       : res[5],
                        'isRecurring'       : res[6],
                        'isCovered'         : res[7],
                        'cancelationPolicy' : res[8],
                        'description'       : res[9],
                        'price'             : float(res[10]),
                        'picture'           : res[11],
                        'rating'            : res[12]}
            self.write(json.dumps(results))