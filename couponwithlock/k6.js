import http from 'k6/http';
import {check} from 'k6';

export const options = {
    scenarios: {
        coupon_issue: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '1s', target: 200 },
                { duration: '30s', target: 600 },
                { duration: '1s', target: 0 }
            ],
        },
    },
};

export default function () {
    const userId = __VU;
    const payload = JSON.stringify({
        userId
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post('http://localhost:8080/api/v1/coupon/issue', payload, params);

    if (res.status !== 200 && res.status !== 404) {
        console.log(`예상 외 응답: ${res.status}, userId: ${userId}, 응답 내용: ${res.body}`);
    }

    if (res.status === 200 || res.status === 404) {
        console.log(`성공: ${res.status}, userId: ${userId}, 응답 내용: ${res.body}`);
    }

    check(res, {
        '정상 응답 (200) 또는 품절 (404)': (r) => r.status === 200 || r.status === 404,
    });
}
