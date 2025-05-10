package com.karhacter.movies_webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.MembershipPackage;

@Repository
public interface MembershipPackageRepository extends JpaRepository<MembershipPackage, Long> {
}
