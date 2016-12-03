package it.poliba.sisinflab.simlib.datamodel.abstat.api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by giorgio on 12/05/16.
 */
public interface AbstatService {

    String SYSTEM_TEST = "system-test";
    //String DBPEDIA_2015_10 = "dbpedia-2015-10";
    String DBPEDIA_2015_10 = "dbpedia-2015-10-infobox";

    //String HOST = "http://abstat.cloudapp.net";
    String HOST = "http://abstat.disco.unimib.it";
    
    String API_PATH = "/api/v1/";

    String DATASET_PARAM = "dataset";
    String SUBJECT_PARAM = "subjectType";
    String PREDICATE_PARAM = "predicate";
    String OBJECT_PARAM = "objectType";
    String RANKING_PARAM = "rankingFunction";
    String LIMIT_PARAM = "limit";
    String FORMAT_PARAM = "format";

    @GET("queryWithParams")
    Call<AbstatResponse> minimalPattern(@Query(DATASET_PARAM) String dataset,
                                        @Query(SUBJECT_PARAM) String subject,
                                        @Query(PREDICATE_PARAM) String predicate,
                                        @Query(OBJECT_PARAM) String object,
                                        @Query(RANKING_PARAM) String ranking,
                                        @Query(LIMIT_PARAM) String limit,
                                        @Query(FORMAT_PARAM) String format);


    static AbstatService serviceFactory(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST + API_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AbstatService.class);

    }

}
