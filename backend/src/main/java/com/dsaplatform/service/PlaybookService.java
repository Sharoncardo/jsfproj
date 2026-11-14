
package com.dsaplatform.service;

import com.dsaplatform.dto.playbook.PlaybookResponse;

import java.util.UUID;

public interface PlaybookService {

    PlaybookResponse getPlaybook(UUID userId);

}
