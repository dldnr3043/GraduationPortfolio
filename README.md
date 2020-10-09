# 목표
android 기반 어플로 openAPI를 통해 거래내역 데이터를 받아와 자동으로 디비, 블록체인에 등록해 사용자에게 보여주고
토큰을 사용해 학생회 활동에 참여하면 토큰을 지급 받을 수 있으며 지급 받은 토큰으로 등록한 물품을 구매할 수 있음

#사용 프로그램
- Android Studio : 프론트 엔드 전반적인 앱 부분 제작
- Remix IDE : solidity 언어로 smart contract를 만들고 MetaMask으로 블록체인 TestNet과 연동하여 TestNet에 contract 배포
- MetaMask : 사용자의 지갑을 관리, Remix와의 연동, 배포한 contract 주소 & 지갑 주소를 통해 Transaction 정보를 볼 수 있음
- Firebase : DB로 firebase 사용
- Web3j : Android Studio에서 블록체인 네트워크에 배포한 contract 내의 함수를 사용하기 위해 자바 파일로 변환, Android Studio에서 contract 사용을
             할 수 있도록 하는 라이브러리

# 전체적인 흐름
    Remix에서 solidity 언어로 smart contract 작성
-> MetaMask와 Remix, Web3j를 통해 사용할 블록체인 네트워크를 연동
-> Remix로 작성한 contract를 TestNet에 배포
-> 배포한 .sol 파일을 solc(=solidity compiler)로 abi, bin 파일로 분리
-> web3j로 분리된 abi, bin 파일을 Android Studio에서 사용할 수 있는 Java 파일로 변환
-> 변환한 Java 파일을 Android Studio 프로젝트 내에 포함
-> Android Studio에서 포함시킨 Java파일을 이용해서 배포된 contract 내의 함수 사용
-> 함수 사용 : 1. 토큰 전송(Admin->User & User->Admin)
	       2. 거래내역 BlockChain Network에 올리기
-> 함수 사용하면 해당 내용을 담고 있는 Transaction이 발생되고 TestNet에선 mining 중이기 때문에
     Transaction이 Block에 포함 되어 Block이 BCNetwork에 올라감