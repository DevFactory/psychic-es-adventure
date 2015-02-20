-- Create an Index and name it
awi

-- Get the mapping for the index with name awi
GET http://t-proto-03.zanox.com:9200/awi/_mapping

-- Set the mapping for the index with name awi
PUT http://t-proto-03.zanox.com:9200/awi/_mapping/impression
{
   "impression" : {
       "properties" : {
           "date" : {
            "type" : "date",
            "format" : "yyyy-MM-dd HH:mm:ss",
            "store" : false
            },
           "merchant_id" : {"type" : "long", "store": false },
           "affiliate_id" : {"type" : "long", "store": false },
           "banner_id" : {"type" : "long", "store": false },
           "group_id" : {"type" : "long", "store": false },
           "is_membership_soft" : {"type" : "integer", "store": false },
           "platform" : {"type" : "string", "store": false }
       }
   }
}

-- Range (2014-01-01 12:00:00 - 2014-01-01 12:00:01
http://t-proto-03.zanox.com:9200/awi_1d/_search
{
    "query" : {
        "range" : {
            "date" : {
                "from" : "2014-01-01 12:00:00",
                "to" : "2014-01-01 12:00:01"
            }
        }
    }
}


-- Aggregation on 1d with range 2014-01-01 - 2014-01-07
http://t-proto-03.zanox.com:9200/awi_7d/impression/_search
{
    "query" : {
        "range" : {
            "date" : {
                "from" : "2014-01-01 00:00:00",
                "to" : "2014-01-07 00:00:00"
            }
        }
    },
	"aggregations" : {
        "impressions_over_time" : {
            "date_histogram" : {
                "field" : "date",
                "interval" : "1d"
            }
        }
    }
}

-- range with search_type=count
http://t-proto-03.zanox.com:9200/awi_1d/_search?search_type=count
{
    "query" : {
        "range" : {
            "date" : {
                "from" : "2014-01-01 00:00:00",
                "to" : "2014-01-01 00:00:00"
            }
        }
    }
}

--  value_countAggregation of merchant_ids for 2014-01-01 - 2014-01-14
http://t-proto-03.zanox.com:9200/awi_14d/_search
{
    "query" : {
        "range" : {
            "date" : {
                "from" : "2014-01-01 00:00:00",
                "to" : "2014-01-14 00:00:00"
            }
        }
    },
    "aggregations" : {
        "merchant_id_count" : { "value_count" :
        	{ "field" : "merchant_id" }
        }
    }
}

-- percentile with range
http://t-proto-03.zanox.com:9200/awi_14d/_search?search_type=count
{
    "query" : {
            "range" : {
                "date" : {
                    "from" : "2014-01-01 00:00:00",
                    "to" : "2014-01-02 00:00:00"
                }
            }
      },
    "aggregations" : {
        "precentile_merchant_ids" : {
            "percentiles" : {
                "field" : "merchant_id"
            }
        }
    }
}

-- avg - doesn't make any sense on merchant_id but for the lulz
http://t-proto-03.zanox.com:9200/awi_14d/_search?search_type=count
{
    "query" : {
            "range" : {
                "date" : {
                    "from" : "2014-01-01 00:00:00",
                    "to" : "2014-01-02 00:00:00"
                }
            }
      },
    "aggregations" : {
             "avg_grade" : { "avg" : { "field" : "merchant_id" } }
         }
}

-- stats  - doesn't make any sense on merchant_id but for the lulz
http://t-proto-03.zanox.com:9200/awi_14d/_search?search_type=count
{
    "aggregations" : {
        "grades_stats" : { "stats" : { "field" : "merchant_id" } }
    }
}

-- extended stats  - doesn't make any sense on merchant_id but for the lulz
{
    "aggregations" : {
        "grades_stats" : { "extended_stats" : { "field" : "merchant_id" } }
    }
}

-- Some combined query - platform has to be cw AND date hast to be in range, then aggregate by merchant_id
http://t-proto-03.zanox.com:9200/awi_1d/impression/_search/
{
   "size": 0,
    "filter": {
    "and": [
      {
        "term": {
          "platform": "cw"
        }
      },
      {
        "range": {
          "date": {
            "from": "2014-01-01 00:00:00",
            "to": "2014-01-02 00:00:00"
          }
        }
      }
   	]
   },
   "aggregations": {
      "group_by_merchant_id": {
         "terms": {
            "field": "merchant_id"

            }
         }
      }
   }
}


