package com.sparta.ssaktium.domain.boards.service;

import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardsSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardsDetailResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardsPageResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardsSaveResponseDto;
import com.sparta.ssaktium.domain.boards.entity.Boards;
import com.sparta.ssaktium.domain.boards.entity.StatusEnum;
import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.boards.repository.BoardsRepository;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.user.entity.User;
import com.sparta.ssaktium.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;
    private final UserService userService;


    @Transactional
    public BoardsSaveResponseDto saveBoards(AuthUser authUser, BoardsSaveRequestDto requestDto) {
        //유저 확인
        User user = userService.findUser(authUser.getUserId());
        //제공받은 정보로 새 보드 만들기
        Boards newBoard = new Boards(requestDto,user);
        //저장
        boardsRepository.save(newBoard);
        //responseDto 반환
        return new BoardsSaveResponseDto(newBoard);
    }

    @Transactional
    public BoardsSaveResponseDto updateBoards(AuthUser authUser, Long id, BoardsSaveRequestDto requestDto) {
        //유저 확인
        User user = userService.findUser(authUser.getUserId());
        //게시글 찾기
        Boards updateBoards = findBoard(id);
        //게시글 본인 확인
       if(!updateBoards.getUser().equals(user)){
           throw new RuntimeException();
       }
       //게시글 수정
       updateBoards.updateBoards(requestDto);
       boardsRepository.save(updateBoards);
       //responseDto 반환
       return new BoardsSaveResponseDto(updateBoards);
    }

    @Transactional
    public void deleteBoards(AuthUser authUser,Long id){
        //유저 확인
        User user = userService.findUser(authUser.getUserId());
        //게시글 찾기
        Boards deleteBoards = findBoard(id);
        //게시글 본인 확인
        if(!deleteBoards.getUser().equals(user)){
            throw new RuntimeException();
        }
        //해당 보드 삭제 상태 변경
        deleteBoards.deleteBoards();
        boardsRepository.save(deleteBoards);
    }

//    //게시글 단건 조회 (댓글필요)
//    public BoardsDetailResponseDto getBoard(Long id) {
//        //게시글 찾기
//       Boards board = findBoard(id);
//        //댓글 찾기
//        List<Comments> commentList = commentRepository.findAllByBoardId(board.getId());
//
//        List<CommentResponseDto> dtoList = new ArrayList<>();
//        for(Comments comments: commentList){
//            dtoList.add(new CommentResponseDto(comments.getId,comments.getContents));
//        }
//
//        return  new BoardsDetailResponseDto(board,dtoList);
//    }

    public BoardsPageResponseDto getMyBoards(AuthUser authUser, int page, int size) {
        //사용자 찾기
        User user = userService.findUser(authUser.getUserId());
        //페이지 요청 객체 생성 (페이지 숫자가 실제로는 0부터 시작하므로 원하는 숫자 -1을 입력해야 해당 페이지가 나온다)
        Pageable pageable = PageRequest.of(page -1, size);
        //해당 유저가 쓴 게시글 페이지네이션해서 가져오기
        Page<Boards> boards = boardsRepository.findAllByUserIdAndStatusEnum(user.getId(), StatusEnum.ACTIVATED,pageable);

        // BoardsPageResponseDto 생성
        return new BoardsPageResponseDto(
                boards.map(BoardsDetailResponseDto::new).getContent(),
                boards.getTotalPages(),
                boards.getTotalElements(),
                boards.getSize(),
                boards.getNumber()
        );
    }

    //뉴스피드 (친구필요)
//    public Page<BoardsDetailResponseDto> getNewsfeed(AuthUser authUser, int page, int size) {
//        //사용자 찾기
//        User user = userService.findUser(authUser.getUserId());
//        Pageable pageable = PageRequest.of(page - 1 ,size);
//
//        //친구목록 가져오기
//        List<User> friends = userService.getFriends(user.getId());
//
//        // 게시글 리스트 가져오기
//        Page<Boards> boardsPage = boardsRepository.findAllForNewsFeed(
//                user,
//                friends,
//                PublicStatus.FRIENDS, // 친구의 게시글 상태
//                PublicStatus.ALL,      // 전체 공개 게시글 상태
//                pageable               // Pageable 객체 전달
//        );
//
//        // DTO로 변환하여 반환
//        return boardsPage.map(BoardsDetailResponseDto::new);
//    }

    //Board 찾는 메서드
    public Boards findBoard(long id){
       return boardsRepository.findById(id).orElseThrow(NotFoundBoardException::new);
    }



}