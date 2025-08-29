package com.example.demo.service;

import com.example.demo.domain.*;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PollManager {

    private final AtomicLong seq = new AtomicLong(1);
    private final Map<Long, UserEntity> users = new ConcurrentHashMap<>();
    private final Map<Long, PollEntity> polls = new ConcurrentHashMap<>();
    private final Map<Long, VoteOptionEntity> options = new ConcurrentHashMap<>();
    private final Map<Long, VoteEntity> votes = new ConcurrentHashMap<>();

    /* Users */
    public UserEntity createUser(UserEntity u){ u.setId(seq.getAndIncrement()); users.put(u.getId(), u); return u; }
    public List<UserEntity> listUsers(){ return new ArrayList<>(users.values()); }
    public Optional<UserEntity> getUser(Long id){ return Optional.ofNullable(users.get(id)); }
    public void deleteUser(Long id){
        users.remove(id);
        votes.values().removeIf(v -> Objects.equals(v.getUserId(), id));
    }

    /* Polls + options */
    public PollEntity createPoll(PollEntity p, List<VoteOptionEntity> newOptions){
        p.setId(seq.getAndIncrement());
        polls.put(p.getId(), p);
        for (VoteOptionEntity o : newOptions){
            o.setId(seq.getAndIncrement());
            o.setPollId(p.getId());
            options.put(o.getId(), o);
            p.getOptionIds().add(o.getId());
        }
        return p;
    }
    public List<PollEntity> listPolls(){ return new ArrayList<>(polls.values()); }
    public Optional<PollEntity> getPoll(Long id){ return Optional.ofNullable(polls.get(id)); }
    public List<VoteOptionEntity> optionsForPoll(Long pollId){
        return getPoll(pollId).map(p ->
                p.getOptionIds().stream().map(options::get).filter(Objects::nonNull).toList()
        ).orElse(List.of());
    }
    public void deletePoll(Long id){
        PollEntity p = polls.remove(id);
        if (p != null){
            p.getOptionIds().forEach(options::remove);
            votes.values().removeIf(v -> Objects.equals(v.getPollId(), id));
        }
    }

    /* Votes */
    public VoteEntity createOrUpdateVote(Long pollId, Long userId, Long optionId){
        votes.values().removeIf(v -> v.getPollId().equals(pollId) && v.getUserId().equals(userId));
        VoteEntity v = VoteEntity.builder()
                .id(seq.getAndIncrement())
                .pollId(pollId).userId(userId).optionId(optionId)
                .publishedAt(Instant.now()).build();
        votes.put(v.getId(), v);
        return v;
    }
    public List<VoteEntity> listVotes(){ return new ArrayList<>(votes.values()); }
}
