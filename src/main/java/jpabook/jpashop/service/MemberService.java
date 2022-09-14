package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//
@Service //컴포넌트 스캔 대상이 되서 자동으로 빈으로 등록됨.
@Transactional(readOnly = true) //읽기 전용
//JPA의 모든 데이터 변경이나 로직들은 트랜잭션 안에서 실행되어야 한다.
@RequiredArgsConstructor
public class MemberService {
    //@Autowired //보통 이렇게 하지만 변경이 안 된다는 단점이 있음.
    private final MemberRepository memberRepository; //변경할 일이 없기 때문에 필드를 final로 고정

    /*
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }//테스트 코드 작성할 때 mock 같은 걸 주입할 수 있음. 필드로만 하면 주입이 불가능
    //단점은 애플리케이션 로딩 시점에 조립이 모두 끝나는데 memberRepository를 변경하지는 않음
    //그래서 좋지 않음.
    */

    //그래서 생성자 인젝션을 사용
    //@Autowired 생성자가 하나만 있는 경우 자동으로 인젝션 해줌.
    //public MemberService(MemberRepository memberRepository) {
    //    this.memberRepository = memberRepository;
    //} (@RequiredArgsConstructor 때문에 생성자 없애줌)
    //한 번 생성할 때 생성이 완성 되기 때무에 중간에 set해서 memberRepository를 바꿀 수 없다.

    //@AllArgsConstructor 를 쓰면 위에 모든 필드에 대한 생성자를 만들어줌
    //@RequiredArgsConstructor 는 final을 가진 필드들에 대한 생성자를 만들어줌 (더 나음)

    //회원 가입
    @Transactional //등록 -> //쓰기 전용
    public Long join(Member member) {
        validateDuplidateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplidateMember(Member member) {
        //문제 생기면 여기서 예외 터뜨림
        List<Member> findMembers = memberRepository.findByName(member.getName()); //memberRepository에서 같은 이름이 있는지를 찾아봄.
        if (!findMembers.isEmpty()) { //비어있지 않으면 (잘못 된 것)
            throw new IllegalStateException("이미 존재하는 회원입니다!!!!!");
        }
    }

    //회원 전체 조회
    //읽기 전용 -> 조회할 때 JPA가 좀 더 최적화해줌
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 한 명만 조회 (단건 조회)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
    //@Transactional(readOnly = true) -> true 해버리면 데이터 변경이 안됌.
}//
