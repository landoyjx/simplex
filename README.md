# simplex

simple exchange demo

kafka is needed, use default local kafka

## interface

- /newOrder, place new order
- /ob/{symbol}, get orderbook for one symbol

## websocket

websocket endpoint path is /market-ws/, include websocket message type: 

- /topic/orderUpdate/{uid}, order update for specified user
- /topci/matchUpdate/{symbol}, match update for one symbol


index.html will be used for test subscribe matchUpdate for BTC_USDT and orderUpdate for user with uid is 1.


