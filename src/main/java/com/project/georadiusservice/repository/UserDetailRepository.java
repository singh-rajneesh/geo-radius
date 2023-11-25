package com.project.georadiusservice.repository;

import com.project.georadiusservice.entity.UserDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public interface UserDetailRepository extends MongoRepository<UserDetail, String> {

    @Query(value = "{ 'loc' : { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] }, $maxDistance: ?2}}}")
    Stream<UserDetail> getNearbyAdvisorsSortedByDistance(Double latitude, Double longitude, Double maxDistanceInMetres);

    default List<UserDetail> getNearbyAdvisors(Double latitude, Double longitude, Integer limit, Integer offset, Double maxDistanceInMetres) {
        return getNearbyAdvisorsSortedByDistance(latitude, longitude, maxDistanceInMetres).skip(offset).limit(limit).collect(Collectors.toList());
    }

}