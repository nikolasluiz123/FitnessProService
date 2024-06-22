import traceback

from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import exception_handler


def custom_exception_handler(exc, context):
    response = exception_handler(exc, context)
    traceback.print_exc()

    if response is not None:
        custom_response_data = {
            'code': response.status_code,
            'message': response.data.get('detail'),
            'errors': response.data,
        }
        response.data = custom_response_data
    else:
        response = Response({
            'code': status.HTTP_500_INTERNAL_SERVER_ERROR,
            'message': 'Ocorreu um erro inesperado, por favor contate o suporte.',
            'details': str(exc),
        }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    return response
