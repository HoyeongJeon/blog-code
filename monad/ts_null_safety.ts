interface User {
	id: number;
	home?: {
		address?: {
			zipcode: string;
		} | null;
	};
}

const userWithoutHome: User = { id: 1};
const userWithHome: User = {id: 2, home: { address: {zipcode: "A123"}}};

const defaultZipcode = "00000"; // 기본값 정의

// 1. 집주소가 없는 사용자 처리
const zipcodeA = userWithoutHome.home?.address?.zipcode ?? defaultZipcode;

console.log(`집주소가 없는 사람의 우편번호 : ${zipcodeA}`); // 출력 00000

// 2. 집주소가 있는 사용자 처리
const zipcodeB = userWithHome.home?.address?.zipcode ?? defaultZipcode;

console.log(`집주소가 있는 사람의 우편번호 : ${zipcodeB}`);
