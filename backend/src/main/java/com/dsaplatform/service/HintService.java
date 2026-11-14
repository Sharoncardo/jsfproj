package com.dsaplatform.service;

import com.dsaplatform.dto.hint.HintProgressDto;

import java.util.UUID;

public interface HintService {

    HintProgressDto getHintProgress(UUID userId, Long problemId);

    HintProgressDto unlockNextHint(UUID userId, Long problemId);

    HintProgressDto resetHintProgress(UUID userId, Long problemId);
}
