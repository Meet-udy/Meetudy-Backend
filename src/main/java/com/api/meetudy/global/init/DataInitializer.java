package com.api.meetudy.global.init;

import com.api.meetudy.chat.entity.Chat;
import com.api.meetudy.chat.entity.ChatRoom;
import com.api.meetudy.chat.entity.ChatRoomMember;
import com.api.meetudy.chat.enums.MessageType;
import com.api.meetudy.chat.repository.ChatRepository;
import com.api.meetudy.chat.repository.ChatRoomRepository;
import com.api.meetudy.comment.entity.Comment;
import com.api.meetudy.comment.repository.CommentRepository;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.enums.LoginType;
import com.api.meetudy.member.repository.MemberRepository;
import com.api.meetudy.post.entity.Post;
import com.api.meetudy.post.enums.PostCategory;
import com.api.meetudy.post.repository.PostRepository;
import com.api.meetudy.group.entity.StudyGroup;
import com.api.meetudy.group.entity.StudyGroupMember;
import com.api.meetudy.group.enums.GroupMemberStatus;
import com.api.meetudy.group.enums.Location;
import com.api.meetudy.group.enums.StudyCategory;
import com.api.meetudy.group.repository.GroupMemberRepository;
import com.api.meetudy.group.repository.GroupRepository;
import com.api.meetudy.interest.entity.Interest;
import com.api.meetudy.interest.entity.MemberInterest;
import com.api.meetudy.interest.repository.InterestRepository;
import com.api.meetudy.interest.repository.MemberInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final InterestRepository interestRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public void run(String... args) throws Exception {
        Member member1 = Member.builder()
                .email("test1@naver.com")
                .username("test123")
                .password(passwordEncoder.encode("Test12345!"))
                .nickname("test1")
                .major("컴퓨터공학과")
                .introduction("안녕하세요, 컴퓨터공학과 재학생입니다!")
                .isOnline(false)
                .location(Location.SEOUL)
                .loginType(LoginType.JWT)
                .roles(List.of("ROLE_USER"))
                .build();

        Member member2 = Member.builder()
                .email("test2@naver.com")
                .username("test1234")
                .password(passwordEncoder.encode("Test12345!"))
                .nickname("test2")
                .major("영어영문학과")
                .introduction("안녕하세요, 영어영문학과 재학생입니다!")
                .isOnline(true)
                .location(Location.GYEONGGIDO)
                .loginType(LoginType.JWT)
                .roles(List.of("ROLE_USER"))
                .build();

        Member member3 = Member.builder()
                .email("test3@naver.com")
                .username("test12345")
                .password(passwordEncoder.encode("Test12345!"))
                .nickname("test3")
                .major("국어국문학과")
                .introduction("안녕하세요, 국어국문학과 재학생입니다!")
                .isOnline(true)
                .location(Location.GYEONGGIDO)
                .loginType(LoginType.JWT)
                .roles(List.of("ROLE_USER"))
                .build();

        Interest interest1 = Interest.builder()
                .studyCategory(StudyCategory.CERTIFICATION)
                .build();

        Interest interest2 = Interest.builder()
                .studyCategory(StudyCategory.PROGRAMMING)
                .build();

        Interest interest3 = Interest.builder()
                .studyCategory(StudyCategory.CAREER)
                .build();

        Interest interest4 = Interest.builder()
                .studyCategory(StudyCategory.LANGUAGE)
                .build();

        MemberInterest memberInterest1 = MemberInterest.builder()
                .member(member1)
                .interest(interest1)
                .build();

        MemberInterest memberInterest2 = MemberInterest.builder()
                .member(member1)
                .interest(interest2)
                .build();

        MemberInterest memberInterest3 = MemberInterest.builder()
                .member(member1)
                .interest(interest3)
                .build();

        MemberInterest memberInterest4 = MemberInterest.builder()
                .member(member2)
                .interest(interest2)
                .build();

        MemberInterest memberInterest5 = MemberInterest.builder()
                .member(member2)
                .interest(interest4)
                .build();

        MemberInterest memberInterest6 = MemberInterest.builder()
                .member(member3)
                .interest(interest4)
                .build();

        StudyGroup studyGroup1 = StudyGroup.builder()
                .name("코테 스터디")
                .description("코딩테스트 준비를 위한 스터디입니다.")
                .duration("2024-12-25 ~ 2025-02-31")
                .maxParticipants(5)
                .isOnline(true)
                .category(StudyCategory.PROGRAMMING)
                .location(Location.OTHERS)
                .build();

        StudyGroup studyGroup2 = StudyGroup.builder()
                .name("토익 스터디")
                .description("토익 준비를 위한 스터디입니다.")
                .duration("2024-12-25 ~ 2025-02-31")
                .maxParticipants(5)
                .isOnline(true)
                .category(StudyCategory.LANGUAGE)
                .location(Location.GYEONGGIDO)
                .build();

        StudyGroup studyGroup3 = StudyGroup.builder()
                .name("정처기 스터디")
                .description("정보처리기사 준비를 위한 스터디입니다.")
                .duration("2024-12-25 ~ 2025-02-31")
                .maxParticipants(5)
                .isOnline(false)
                .category(StudyCategory.CERTIFICATION)
                .location(Location.GYEONGGIDO)
                .build();

        StudyGroup studyGroup4 = StudyGroup.builder()
                .name("취준 스터디")
                .description("취업 준비를 위한 스터디입니다.")
                .duration("2024-12-25 ~ 2025-02-31")
                .maxParticipants(5)
                .isOnline(false)
                .category(StudyCategory.CAREER)
                .location(Location.SEOUL)
                .build();

        StudyGroup studyGroup5 = StudyGroup.builder()
                .name("사회과학 스터디")
                .description("사회과학 스터디입니다.")
                .duration("2025-03-01 ~ 2025-05-01")
                .maxParticipants(10)
                .isOnline(true)
                .category(StudyCategory.SOCIAL_SCIENCES)
                .location(Location.OTHERS)
                .build();

        StudyGroup studyGroup6 = StudyGroup.builder()
                .name("피그마 스터디")
                .description("피그마 스터디입니다.")
                .duration("2025-03-01 ~ 2025-05-01")
                .maxParticipants(10)
                .isOnline(true)
                .category(StudyCategory.DESIGN)
                .location(Location.OTHERS)
                .build();

        StudyGroupMember groupMember1 = StudyGroupMember.builder()
                .status(GroupMemberStatus.LEADER)
                .member(member1)
                .studyGroup(studyGroup1)
                .build();

        StudyGroupMember groupMember2 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member2)
                .studyGroup(studyGroup1)
                .build();

        StudyGroupMember groupMember3 = StudyGroupMember.builder()
                .status(GroupMemberStatus.LEADER)
                .member(member1)
                .studyGroup(studyGroup2)
                .build();

        StudyGroupMember groupMember4 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member2)
                .studyGroup(studyGroup2)
                .build();

        StudyGroupMember groupMember5 = StudyGroupMember.builder()
                .status(GroupMemberStatus.LEADER)
                .member(member2)
                .studyGroup(studyGroup3)
                .build();

        StudyGroupMember groupMember6 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member1)
                .studyGroup(studyGroup3)
                .build();

        StudyGroupMember groupMember7 = StudyGroupMember.builder()
                .status(GroupMemberStatus.LEADER)
                .member(member2)
                .studyGroup(studyGroup4)
                .build();

        StudyGroupMember groupMember8 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member1)
                .studyGroup(studyGroup4)
                .build();

        StudyGroupMember groupMember9 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member3)
                .studyGroup(studyGroup1)
                .build();

        StudyGroupMember groupMember10 = StudyGroupMember.builder()
                .status(GroupMemberStatus.REQUESTED)
                .member(member3)
                .studyGroup(studyGroup2)
                .build();

        StudyGroupMember groupMember11 = StudyGroupMember.builder()
                .status(GroupMemberStatus.MEMBER)
                .member(member3)
                .studyGroup(studyGroup1)
                .build();

        StudyGroupMember groupMember12 = StudyGroupMember.builder()
                .status(GroupMemberStatus.LEADER)
                .member(member3)
                .studyGroup(studyGroup6)
                .build();

        ChatRoom chatRoom1 = ChatRoom.builder()
                .groupName("코테 스터디")
                .isPrivate(false)
                .studyGroup(studyGroup1)
                .build();

        ChatRoom chatRoom2 = ChatRoom.builder()
                .groupName("정처기 스터디")
                .isPrivate(false)
                .studyGroup(studyGroup3)
                .build();

        ChatRoom chatRoom3 = ChatRoom.builder()
                .groupName("취준 스터디")
                .isPrivate(false)
                .studyGroup(studyGroup4)
                .build();

        Chat chat1 = Chat.builder()
                .message("안녕하세요!")
                .messageType(MessageType.TALK)
                .sender(member1)
                .room(chatRoom1)
                .build();

        Chat chat2 = Chat.builder()
                .message("저도 안녕하세요!")
                .messageType(MessageType.TALK)
                .sender(member2)
                .room(chatRoom1)
                .build();

        Chat chat3 = Chat.builder()
                .message("넵 안녕하세요!")
                .messageType(MessageType.TALK)
                .sender(member3)
                .room(chatRoom1)
                .build();

        Chat chat4 = Chat.builder()
                .message("안녕하세요!")
                .messageType(MessageType.TALK)
                .sender(member3)
                .room(chatRoom2)
                .build();

        Chat chat5 = Chat.builder()
                .message("반갑습니다!")
                .messageType(MessageType.TALK)
                .sender(member1)
                .room(chatRoom2)
                .build();

        Chat chat6 = Chat.builder()
                .message("안녕하세요!")
                .messageType(MessageType.TALK)
                .sender(member2)
                .room(chatRoom2)
                .build();

        chatRoom1.addChatRoomMember(ChatRoomMember.of("코테 스터디", chatRoom1, member1));
        chatRoom1.addChatRoomMember(ChatRoomMember.of("코테 스터디", chatRoom1, member2));
        chatRoom1.addChatRoomMember(ChatRoomMember.of("코테 스터디", chatRoom1, member3));
        chatRoom2.addChatRoomMember(ChatRoomMember.of("정처기 스터디", chatRoom2, member1));
        chatRoom2.addChatRoomMember(ChatRoomMember.of("정처기 스터디", chatRoom2, member3));
        chatRoom3.addChatRoomMember(ChatRoomMember.of("취준 스터디", chatRoom2, member1));
        chatRoom3.addChatRoomMember(ChatRoomMember.of("취준 스터디", chatRoom2, member2));

        Post post1 = Post.builder()
                .title("영어 공부 꿀팁")
                .content("영어 공부 꿀팁 소개합니다.")
                .postCategory(PostCategory.STUDY_TIP)
                .author(member1)
                .build();

        Post post2 = Post.builder()
                .title("코테 스터디 홍보")
                .content("코테 스터디 홍보합니다.")
                .postCategory(PostCategory.STUDY_PROMOTION)
                .author(member2)
                .build();

        Post post3 = Post.builder()
                .title("공모전 팀원 모집")
                .content("공모전 팀원 모집합니다.")
                .postCategory(PostCategory.GENERAL)
                .author(member3)
                .build();

        Comment comment1 = Comment.builder()
                .content("감사합니다.")
                .author(member2)
                .post(post1)
                .build();

        Comment comment2 = Comment.builder()
                .content("팁 감사합니다!")
                .author(member3)
                .post(post1)
                .build();

        memberRepository.saveAll(Arrays.asList(member1, member2, member3));
        interestRepository.saveAll(Arrays.asList(interest1, interest2, interest3, interest4));
        memberInterestRepository.saveAll(Arrays.asList(memberInterest1, memberInterest2, memberInterest3, memberInterest4, memberInterest5, memberInterest6));
        groupRepository.saveAll(Arrays.asList(studyGroup1, studyGroup2, studyGroup3, studyGroup4, studyGroup5, studyGroup6));
        groupMemberRepository.saveAll(Arrays.asList(groupMember1, groupMember2, groupMember3, groupMember4, groupMember5, groupMember6, groupMember7, groupMember8, groupMember9, groupMember10, groupMember11, groupMember12));
        chatRoomRepository.saveAll(Arrays.asList(chatRoom1, chatRoom2, chatRoom3));
        chatRepository.saveAll(Arrays.asList(chat1, chat2, chat3, chat4, chat5, chat6));
        postRepository.saveAll(Arrays.asList(post1, post2, post3));
        commentRepository.saveAll(Arrays.asList(comment1, comment2));
    }

}