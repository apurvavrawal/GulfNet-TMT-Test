package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SettingsRepository extends JpaRepository<Settings, UUID> {
}
